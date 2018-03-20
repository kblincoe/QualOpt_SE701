import { BaseEntity, User } from './../../shared';

export class Study implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public description?: any,
        public incentive?: any,
        public status?: Status,
        public emailSubject?: string,
        public emailBody?: any,
        public user?: User,
        public participants?: BaseEntity[],
    ) {
    }
}

export enum Status {
    INACTIVE = 'Inactive', // When no emails have been sent out.
    ACTIVE = 'Active', // When emails have been sent out.
    COMPLETED = 'Completed' // When the study has finished and no more changes are required.
}
