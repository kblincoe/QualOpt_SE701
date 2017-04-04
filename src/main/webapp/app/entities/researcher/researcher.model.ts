import { Study } from '../study';
export class Researcher {
    constructor(
        public id?: number,
        public email?: string,
        public password?: string,
        public profession?: string,
        public institution?: string,
        public mailServer?: string,
        public study?: Study,
    ) {
    }
}
