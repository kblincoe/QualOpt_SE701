import { BaseEntity } from '../../../shared';

/** Enum representing the type of user selected in the dropdown box
 * in the Github User Search Form.
 */
export enum UserType {
    USER,
    ORG,
    NOPREFERNCE
}

/** Simple object for maintaining the infomration that the user enters
 * in the GitHub User Search Form.
 *
 * This class uses strings for numberOfRepositories and numberOfFollowers
 * as they can be optionally prefixed with ">,>=,<,<=" - and are passed into
 * the API request as a string as well.
 *
 * The requestedContributionCount boolean is there to alert users that searching
 * up contribution counts can be very exhaustive (due to the REST nature of the current
 * call to the GitHub API)
 */
export class GitHubAPIRequest implements BaseEntity {
    constructor(
        public id?: number,
        public keyword?: string,
        public userType?: UserType,
        public numberOfRepositories?: string,
        public location?: string,
        public joinedBeforeDate?: string,
        public programmingLanguage?: string,
        public numberOfFollowers?: string,
        public requestedContributionCount?: boolean,
        public maxNumberOfUserLookups?: number
    ) {
        this.userType = UserType.NOPREFERNCE;
        this.maxNumberOfUserLookups = 0;
    }
}
