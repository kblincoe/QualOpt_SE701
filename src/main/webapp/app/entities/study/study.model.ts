import { Researcher } from '../researcher';
export class Study {
    constructor(
        public id?: number,
        public name?: string,
        public description?: string,
        public incentive?: string,
        public hasPay?: boolean,
        public researcher?: Researcher,
    ) {
        this.hasPay = false;
    }
}
