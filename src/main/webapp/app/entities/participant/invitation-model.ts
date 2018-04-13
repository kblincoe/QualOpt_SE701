import { Study } from "../study";
/*
 * Invitation class is used as a domain object
 * to post a study and a delay
 */
export class Invitation {
    constructor(
        public study?: Study,
        public delay?: number
    ) {
    }
}

