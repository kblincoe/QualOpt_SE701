import { BaseEntity, User } from './../../shared';

export class Study implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public description?: any,
        public incentive?: any,
        public email?: BaseEntity,
        public user?: User,
        public participants?: BaseEntity[],
    ) {
    }
}
