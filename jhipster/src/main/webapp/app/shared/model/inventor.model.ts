import { IDisciplineSkill } from 'app/shared/model/discipline-skill.model';
import { IInvention } from 'app/shared/model/invention.model';

export interface IInventor {
  id?: number;
  name?: string | null;
  timeModifer?: number | null;
  costModifier?: number | null;
  disciplineSkills?: IDisciplineSkill[] | null;
  invention?: IInvention | null;
}

export const defaultValue: Readonly<IInventor> = {};
