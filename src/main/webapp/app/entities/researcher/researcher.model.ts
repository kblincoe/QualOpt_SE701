import { BaseEntity } from './../../shared';

export class Researcher implements BaseEntity {
    constructor(
        public id?: number,
        public emailAddress?: string,
        public occupation?: string,
        public institute?: string,
        public studies?: BaseEntity[],
    ) {
    }
}
