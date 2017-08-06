import { BaseEntity } from './../../shared';

export class Study implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public description?: string,
        public incentive?: string,
        public hasPay?: boolean,
        public email?: BaseEntity,
        public participants?: BaseEntity[],
        public researcher?: BaseEntity,
    ) {
        this.hasPay = false;
    }
}
