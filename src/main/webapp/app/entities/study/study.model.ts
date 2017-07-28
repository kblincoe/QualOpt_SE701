import { BaseEntity } from './../../shared';

export class Study implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public description?: string,
        public incentive?: string,
        public hasPay?: boolean,
        public researchers?: BaseEntity[],
        public participants?: BaseEntity[],
    ) {
        this.hasPay = false;
    }
}
