import { BaseEntity } from './../../shared';

export class Email implements BaseEntity {
    constructor(
        public id?: number,
        public subject?: string,
        public body?: any,
    ) {
    }
}
