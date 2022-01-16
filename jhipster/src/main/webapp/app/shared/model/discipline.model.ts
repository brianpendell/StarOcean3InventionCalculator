import { IDisciplineSkill } from 'app/shared/model/discipline-skill.model';
import { IInvention } from 'app/shared/model/invention.model';

export interface IDiscipline {
  id?: number;
  name?: string | null;
  helperDevice?: string | null;
  disciplineSkills?: IDisciplineSkill[] | null;
  inventions?: IInvention[] | null;
}

export const defaultValue: Readonly<IDiscipline> = {};
