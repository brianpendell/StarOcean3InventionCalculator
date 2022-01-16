import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Discipline from './discipline';
import DisciplineDetail from './discipline-detail';
import DisciplineUpdate from './discipline-update';
import DisciplineDeleteDialog from './discipline-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={DisciplineUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={DisciplineUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={DisciplineDetail} />
      <ErrorBoundaryRoute path={match.url} component={Discipline} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={DisciplineDeleteDialog} />
  </>
);

export default Routes;
