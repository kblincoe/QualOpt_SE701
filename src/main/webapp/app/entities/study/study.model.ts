import { BaseEntity, User } from './../../shared';
import { Document } from '../document'

export class Study implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public description?: any,
        public incentive?: any,
        public status?: Status,
        public bouncedMail?: string,
        public emailSubject?: string,
        public emailBody?: any,
        public faq?: string,
        public user?: User,
        public participants?: BaseEntity[],
        public documents?: Document[]
    ) {
        if(this.documents===undefined) {
            this.documents=[];
        }
    }
}

export class StudyInfo {
    constructor(
        public name?: string,
        public faq?: string,
        public documents?: Document[]
    ) {
    }
}

export enum Status {
    INACTIVE = 'Inactive', // When no emails have been sent out.
    ACTIVE = 'Active', // When emails have been sent out.
    COMPLETED = 'Completed' // When the study has finished and no more changes are required.
}
