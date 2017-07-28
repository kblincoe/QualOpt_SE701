import { BaseEntity } from './../../shared';

export class Researcher implements BaseEntity {
    constructor(
        public id?: number,
        public email?: string,
        public occupation?: string,
        public institute?: string,
        public study?: BaseEntity,
    ) {
    }
}
