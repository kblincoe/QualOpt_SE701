package org.project36.qualopt.service;

import org.project36.qualopt.domain.Authority;
import org.project36.qualopt.domain.User;
import org.project36.qualopt.repository.AuthorityRepository;
import org.project36.qualopt.repository.UserRepository;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.UserProfile;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import javax.annotation.PostConstruct;

import org.project36.qualopt.domain.Participant;
import org.project36.qualopt.repository.ParticipantRepository;

@Service
public class SocialService {

    private final Logger log = LoggerFactory.getLogger(SocialService.class);

    private final UsersConnectionRepository usersConnectionRepository;

    private final AuthorityRepository authorityRepository;

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    private final MailService mailService;

    private final ParticipantRepository participantRepository;

    public SocialService(UsersConnectionRepository usersConnectionRepository, AuthorityRepository authorityRepository,
            PasswordEncoder passwordEncoder, UserRepository userRepository,
            MailService mailService, ParticipantRepository participantRepository) {

        this.usersConnectionRepository = usersConnectionRepository;
        this.authorityRepository = authorityRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.mailService = mailService;
        this.participantRepository = participantRepository;
    }

    public void deleteUserSocialConnection(String login) {
        ConnectionRepository connectionRepository = usersConnectionRepository.createConnectionRepository(login);
        connectionRepository.findAllConnections().keySet().stream()
            .forEach(providerId -> {
                connectionRepository.removeConnections(providerId);
                log.debug("Delete user social connection providerId: {}", providerId);
            });
    }

    public void createSocialUser(Connection<?> connection, String langKey) {
        if (connection == null) {
            log.error("Cannot create social user because connection is null");
            throw new IllegalArgumentException("Connection cannot be null");
        }
        UserProfile userProfile = connection.fetchUserProfile();
        String providerId = connection.getKey().getProviderId();
        String imageUrl = connection.getImageUrl();

        //In the future, this can be adapted to hold more authorities (e.g. Researcher and Participant at the same time)
        Set<Authority> authorities = new HashSet<>(1);
        boolean isEmailAssociatedWithParticipant = checkIfParticipantExists(userProfile.getEmail());  

        //If this email is associated with a participant, then assign the ROLE: PARTICIPANT
        if (isEmailAssociatedWithParticipant) {
            authorities.add(authorityRepository.findById("ROLE_PARTICIPANT").orElseGet(() -> {
                return null;
            }));
        } else {
            //Currently, if you are using github, you are assumed to be a participant rather than a researcher
            if (connection.getKey().getProviderId() == "github") {
                authorities.add(authorityRepository.findById("ROLE_PARTICIPANT").orElseGet(() -> {
                    return null;
                }));
                createParticipant(userProfile.getEmail());
            } else {
                authorities.add(authorityRepository.findById("ROLE_USER").orElseGet(() -> {
                    return null;
                }));
            }
        }

        User user = createUserIfNotExist(userProfile, langKey, providerId, imageUrl, authorities);
        createSocialConnection(user.getLogin(), connection);
        mailService.sendSocialRegistrationValidationEmail(user, providerId);
    }

    private User createUserIfNotExist(UserProfile userProfile, String langKey, String providerId, String imageUrl, Set<Authority> authorities) {
        String email = userProfile.getEmail();
        String userName = userProfile.getUsername();
        if (!StringUtils.isBlank(userName)) {
            userName = userName.toLowerCase(Locale.ENGLISH);
        }
        if (StringUtils.isBlank(email) && StringUtils.isBlank(userName)) {
            log.error("Cannot create social user because email and login are null");
            throw new IllegalArgumentException("Email and login cannot be null");
        }
        if (StringUtils.isBlank(email) && userRepository.findOneByLogin(userName).isPresent()) {
            log.error("Cannot create social user because email is null and login already exist, login -> {}", userName);
            throw new IllegalArgumentException("Email cannot be null with an existing login");
        }
        if (!StringUtils.isBlank(email)) {
            Optional<User> user = userRepository.findOneByEmail(email);
            if (user.isPresent()) {
                log.info("User already exist associate the connection to this account");
                return user.get();
            }
        }

        String login = getLoginDependingOnProviderId(userProfile, providerId);
        String encryptedPassword = passwordEncoder.encode(RandomStringUtils.random(10));

        User newUser = new User();
        newUser.setLogin(login);
        newUser.setPassword(encryptedPassword);
        newUser.setFirstName(userProfile.getFirstName());
        newUser.setLastName(userProfile.getLastName());
        newUser.setEmail(email);
        newUser.setActivated(true);
        newUser.setAuthorities(authorities);
        newUser.setLangKey(langKey);
        newUser.setImageUrl(imageUrl);

        return userRepository.save(newUser);
    }

    /**
     * This is a helper method to create a participant with a given email. The participant created will have
     * default values other than the email and then will be saved to the database. 
     * @param email - The string representation of the email to be associated with the participant
     */
    private void createParticipant(String email) {
        Participant newParticipant = new Participant();
        newParticipant.setEmail(email);
        participantRepository.save(newParticipant);
    }

    /**
     * This is a helper method to see if a participant exists in the database using a given email.
     * @param email - The string representation of the email that may be associated with the participant.
     * @return - A boolean variable that is true if the participant exists.
     */
    private boolean checkIfParticipantExists(String email) {
        Participant participant = participantRepository.findOneByEmail(email);
        return (participant != null ? true : false);
    }

    /**
     * @return login if provider manage a login like Twitter or GitHub otherwise email address.
     *         Because provider like Google or Facebook didn't provide login or login like "12099388847393"
     */
    private String getLoginDependingOnProviderId(UserProfile userProfile, String providerId) {
        switch (providerId) {
            case "twitter":
                return userProfile.getUsername().toLowerCase();
            default:
                return userProfile.getEmail();
        }
    }

    private void createSocialConnection(String login, Connection<?> connection) {
        ConnectionRepository connectionRepository = usersConnectionRepository.createConnectionRepository(login);
        connectionRepository.addConnection(connection);
    }

    // Reflective method to prevent the Facebook connection from picking invalid fields when fetching user data
    @PostConstruct
    private void init() {
        try {
            String[] fieldsToMap = { "id", "about", "age_range", "birthday",
                    "context", "cover", "currency", "devices", "education",
                    "email", "favorite_athletes", "favorite_teams",
                    "first_name", "gender", "hometown", "inspirational_people",
                    "installed", "install_type", "is_verified", "languages",
                    "last_name", "link", "locale", "location", "meeting_for",
                    "middle_name", "name", "name_format", "political",
                    "quotes", "payment_pricepoints", "relationship_status",
                    "religion", "security_settings", "significant_other",
                    "sports", "test_group", "timezone", "third_party_id",
                    "updated_time", "verified", "viewer_can_send_gift",
                    "website", "work" };

            Field field = Class.forName(
                    "org.springframework.social.facebook.api.UserOperations")
                    .getDeclaredField("PROFILE_FIELDS");
            field.setAccessible(true);

            Field modifiers = field.getClass().getDeclaredField("modifiers");
            modifiers.setAccessible(true);
            modifiers.setInt(field, field.getModifiers() & ~Modifier.FINAL);
            field.set(null, fieldsToMap);

        } catch (Exception e) {
            log.error("Failed to update user info fields for the facebook api connector", e);
        }
    }
}
