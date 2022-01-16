import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import DisciplineSkill from './discipline-skill';
import DisciplineSkillDetail from './discipline-skill-detail';
import DisciplineSkillUpdate from './discipline-skill-update';
import DisciplineSkillDeleteDialog from './discipline-skill-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={DisciplineSkillUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={DisciplineSkillUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={DisciplineSkillDetail} />
      <ErrorBoundaryRoute path={match.url} component={DisciplineSkill} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={DisciplineSkillDeleteDialog} />
  </>
);

export default Routes;
