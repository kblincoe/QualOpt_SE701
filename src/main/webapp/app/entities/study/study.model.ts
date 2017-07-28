import { BaseEntity } from './../../shared';

export class Study implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public description?: string,
        public incentive?: string,
        public researchers?: BaseEntity[],
        public participants?: BaseEntity[],
    ) {
    }
}
