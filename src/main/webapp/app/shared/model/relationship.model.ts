import { IProfile } from 'app/shared/model/profile.model';

export interface IRelationship {
  id?: number;
  status?: string;
  users?: IProfile[];
}

export const defaultValue: Readonly<IRelationship> = {};
