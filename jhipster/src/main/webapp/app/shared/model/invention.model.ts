import { IInventor } from 'app/shared/model/inventor.model';
import { IDiscipline } from 'app/shared/model/discipline.model';

export interface IInvention {
  id?: number;
  name?: string | null;
  cost?: number | null;
  difficulty?: number | null;
  inventors?: IInventor[] | null;
  discipline?: IDiscipline | null;
}

export const defaultValue: Readonly<IInvention> = {};
