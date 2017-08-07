import { BaseEntity } from './../../shared';

export class Participant implements BaseEntity {
    constructor(
        public id?: number,
        public email?: string,
        public occupation?: string,
        public location?: string,
        public programmingLanguage?: string,
        public numberOfContributions?: number,
        public numberOfRepositories?: number,
        public studies?: BaseEntity[],
    ) {
    }
}
