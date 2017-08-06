import { BaseEntity } from './../../shared';

export class Participant implements BaseEntity {
    constructor(
        public id?: number,
        public emailAddress?: string,
        public occupation?: string,
        public studies?: BaseEntity[],
    ) {
    }
}
