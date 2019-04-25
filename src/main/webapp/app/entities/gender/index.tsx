import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Gender from './gender';
import GenderDetail from './gender-detail';
import GenderUpdate from './gender-update';
import GenderDeleteDialog from './gender-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={GenderUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={GenderUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={GenderDetail} />
      <ErrorBoundaryRoute path={match.url} component={Gender} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={GenderDeleteDialog} />
  </>
);

export default Routes;
