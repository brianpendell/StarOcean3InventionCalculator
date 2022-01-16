import React from 'react';
import { Switch } from 'react-router-dom';

// eslint-disable-next-line @typescript-eslint/no-unused-vars
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Discipline from './discipline';
import DisciplineSkill from './discipline-skill';
import Inventor from './inventor';
import Invention from './invention';
/* jhipster-needle-add-route-import - JHipster will add routes here */

const Routes = ({ match }) => (
  <div>
    <Switch>
      {/* prettier-ignore */}
      <ErrorBoundaryRoute path={`${match.url}discipline`} component={Discipline} />
      <ErrorBoundaryRoute path={`${match.url}discipline-skill`} component={DisciplineSkill} />
      <ErrorBoundaryRoute path={`${match.url}inventor`} component={Inventor} />
      <ErrorBoundaryRoute path={`${match.url}invention`} component={Invention} />
      {/* jhipster-needle-add-route-path - JHipster will add routes here */}
    </Switch>
  </div>
);

export default Routes;
