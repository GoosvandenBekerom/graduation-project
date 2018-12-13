import {SecurityGroup} from './security-group';

export class Resource {
    id: number;
    securityGroups: SecurityGroup[];
}
