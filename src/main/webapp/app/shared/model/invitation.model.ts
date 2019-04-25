import { Moment } from 'moment';
import { IProfile } from 'app/shared/model/profile.model';

export interface IInvitation {
  id?: number;
  createdDate?: Moment;
  accepted?: boolean;
  sent?: IProfile;
  received?: IProfile;
}

export const defaultValue: Readonly<IInvitation> = {
  accepted: false
};
