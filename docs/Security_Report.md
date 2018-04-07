# Security Investigations

## Investigating unauthorised access to user's usernames, passwords, and emails
> Carried out by Frederick Fogerty on 7 April 2018
### Introduction
The security task statement was as follows:
> As QualOpt saves users' login, emails and passwords, there needs to be sufficient security to ensure none of the information can be accessed by a third party. Investigate how this information is stored and how easy it would be for an unsolicited user to access.

### Possible Attack Vectors
QualOpt is a public web application, hosted on a Java web server in the cloud. This presents a large surface, leading to potentially many attack vectors. The attack surface is as follows:

- Public website
- HTTP API
- Software dependencies
- Database
- Physical System/Cloud Provider

The public website and API attack surfaces will be discussed in more detail. Discussing the security of the Software Dependencies, and the Physical System is outside the scope of this report. These systems can be assumed to be secure given correct configuration.

#### Public Website
The public website is a thick-client, which interacts with the backend API over HTTP. The frontend does not have direct access to any other systems.

Because of this isolated nature, we can be assured of the security of the frontend by sufficiently securing the API. As long as the API is secure, no bugs in the frontend could cause a security leak.

#### HTTP API
The HTTP API is the most significant and most likely attack vector for an unsolicited user to try gain access to the system.

The API is built according to the REST standard. Each route is configured to a different level of security. There are three security levels present in the application: un-secured, authenticated, and admin.

**Un-secured:** able to be accessed without authentication.  
**Authenticated:** able to be accessed by any normal, authenticated user.   
**Admin:** reserved for the admin account. This admin account is accessed by logging in with the administrator credentials, which are set on the deployment of the application.

The Spring Java code to configure the API security levels located in `/src/main/java/org/project36/qualopt/config/SecurityConfiguration.java`, and is quoted below
```
.antMatchers("/api/register").permitAll()
.antMatchers("/api/activate").permitAll()
.antMatchers("/api/authenticate").permitAll()
.antMatchers("/api/account/reset_password/init").permitAll()
.antMatchers("/api/account/reset_password/finish").permitAll()
.antMatchers("/api/profile-info").permitAll()
.antMatchers("/api/studies/**/info").permitAll()
.antMatchers("/api/documents/**/download").permitAll()
.antMatchers("/api/**").authenticated()
.antMatchers("/management/health").permitAll()
.antMatchers("/management/**").hasAuthority(AuthoritiesConstants.ADMIN)
.antMatchers("/v2/api-docs/**").permitAll()
.antMatchers("/swagger-resources/configuration/ui").permitAll()
.antMatchers("/swagger-ui/index.html").hasAuthority(AuthoritiesConstants.ADMIN);
```
The un-secured routes are: 
- api/register
- api/activate
- api/authenticate
- api/account/reset_password/init
- api/account/reset_password/finish 
- api/profile-info
- api/studies/**/info
- api/documents/**/download
- management/health
- /v2/api-docs/**
- swagger-resources/configuration/ui

None of these unsecured routes return user information and do not pose a security risk.

The authenticated routes which return user information are:
- api/users - lists all users of the system. Data includes login and email. Potential attack vector.
- api/users/{login} - returns information about a user. Data includes login and email. Potential attack vector.
- api/account - returns information about the currently logged-in user. Not an attack vector.

This report does not investigate administrator-only routes, as it is assumed that the administrator account is not able to be accessed by an unsolicited user. 

The process of becoming an authenticated user is easy for an unsolicited user. The unsolicited user must first create an account using `api/register` with a working email address. After the unsolicited user verifies his email address, the new account is created, and it can be used to log in. Upon logging in, the unsolicited user is now authenticated and can explore users' data, including sensitive data (email), using both `api/users` and `api/users/{login}`. 

#### Database
The storage of user data is in a database, in the `JHI_USER` table. The username and email are stored in plain-text, and the password is stored in a hashed form. The hashing algorithm used is [BCrypt](https://en.wikipedia.org/wiki/Bcrypt), provided by the module 
`org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder`, and is configured to use the default strength of `10`. This hashing configuration provides sufficient security in the storage of the password.

As the password is stored in a hashed form, this implicates that the plaintext of the password would not be able to be retrieved from the system without also breaking the hashing algorithm used. As the hashing algorithm used is industry-standard, this is a secure method of storage.

#### Conclusion
The purpose of this report is to investigate the ability of an unsolicited user to access sensitive user information - their username, email, and password. After investigating the QualOpt system, it has been found that an unsolicited user is able to create an account on the platform without any approval, and using this account, is able to access the usernames and emails of every user of the system. Passwords were not able to be accessed through the API. Passwords were also found to be stored in a secure form, preventing the real password from being able to be retrieved, even if the database was to be accessed directly.











