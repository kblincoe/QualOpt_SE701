import { BaseEntity } from './../../shared';

export class Document implements BaseEntity {
    constructor(
        public id?: number,
        public filename?: string,
        public fileimage?: string
    ) {
    }
}
