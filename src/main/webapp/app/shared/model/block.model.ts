import { Moment } from 'moment';
import { IProfile } from 'app/shared/model/profile.model';

export interface IBlock {
  id?: number;
  createdDate?: Moment;
  sent?: IProfile;
  received?: IProfile;
}

export const defaultValue: Readonly<IBlock> = {};
