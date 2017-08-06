import { BaseEntity, User } from './../../shared';

export class Study implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public description?: any,
        public incentive?: any,
        public hasPay?: boolean,
        public user?: User,
    ) {
        this.hasPay = false;
    }
}
