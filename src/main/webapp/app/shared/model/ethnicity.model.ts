import { IProfile } from 'app/shared/model/profile.model';

export interface IEthnicity {
  id?: number;
  ethnicity?: string;
  users?: IProfile[];
}

export const defaultValue: Readonly<IEthnicity> = {};
