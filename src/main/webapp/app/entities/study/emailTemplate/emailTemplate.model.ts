import { User } from '../../../shared';

export class EmailTemplate {
    constructor(
        public id?: number,
        public name?: string,
        //for some reason, the name of the fields in the json objects is "subject" 
        //instead of "email_subject" or "emailSubject"
        //So this must match. 
        public subject?: string,
        public body?: string,
        public user?: User,
    ) {
    }
}