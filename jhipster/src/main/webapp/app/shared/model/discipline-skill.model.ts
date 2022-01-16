import { IInventor } from 'app/shared/model/inventor.model';
import { IDiscipline } from 'app/shared/model/discipline.model';

export interface IDisciplineSkill {
  id?: number;
  skillModifier?: number | null;
  inventor?: IInventor | null;
  discipline?: IDiscipline | null;
}

export const defaultValue: Readonly<IDisciplineSkill> = {};
