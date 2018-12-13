import { MenuItem } from '../../../models/menu-item';
import {Serializer} from './serializer';

export class MenuItemSerializer implements Serializer<MenuItem> {
    toJson = ({ text, icon }: MenuItem) => ({ text, icon });
}
