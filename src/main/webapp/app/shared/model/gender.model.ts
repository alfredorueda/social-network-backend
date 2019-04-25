import { IProfile } from 'app/shared/model/profile.model';

export interface IGender {
  id?: number;
  type?: string;
  users?: IProfile[];
}

export const defaultValue: Readonly<IGender> = {};
