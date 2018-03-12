import { BaseEntity, User } from './../../shared';

enum IncentiveType {
    'One-off payment',
    'Randomly allocated',
    'Other'
}

export class Study implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public description?: any,
        public incentiveType?: IncentiveType,
        public incentiveDetail?: any,
        public emailSubject?: string,
        public emailBody?: any,
        public user?: User,
        public participants?: BaseEntity[],
    ) {
    }
}
