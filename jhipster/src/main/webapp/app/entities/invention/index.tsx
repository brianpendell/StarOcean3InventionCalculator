import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Invention from './invention';
import InventionDetail from './invention-detail';
import InventionUpdate from './invention-update';
import InventionDeleteDialog from './invention-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={InventionUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={InventionUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={InventionDetail} />
      <ErrorBoundaryRoute path={match.url} component={Invention} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={InventionDeleteDialog} />
  </>
);

export default Routes;
