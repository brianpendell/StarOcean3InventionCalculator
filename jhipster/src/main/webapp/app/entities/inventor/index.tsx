import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Inventor from './inventor';
import InventorDetail from './inventor-detail';
import InventorUpdate from './inventor-update';
import InventorDeleteDialog from './inventor-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={InventorUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={InventorUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={InventorDetail} />
      <ErrorBoundaryRoute path={match.url} component={Inventor} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={InventorDeleteDialog} />
  </>
);

export default Routes;
