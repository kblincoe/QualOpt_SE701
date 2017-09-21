import {Pipe, PipeTransform} from '@angular/core';
import {Participant} from './participant.model'

@Pipe({
    name: 'participant',
    pure: false
})
export class ParticipantPipe implements PipeTransform {
    transform(participants: Participant[], filter: Participant): Participant[] {
        if (!participants || !filter) {
            return participants;
        }
        return participants.filter((participant: Participant) => this.applyFilter(participant, filter));
    }

    applyFilter(participant: Participant, filter: Participant): boolean {
        for (const field in filter) {
            if (filter[field]) {
                if (typeof filter[field] === 'string') {
                    if (participant[field].toLowerCase().indexOf(filter[field].toLowerCase()) === -1) {
                        return false;
                    }
                } else if (typeof filter[field] === 'number') {
                    if (participant[field] < filter[field]) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
