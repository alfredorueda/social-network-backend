import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Ethnicity from './ethnicity';
import EthnicityDetail from './ethnicity-detail';
import EthnicityUpdate from './ethnicity-update';
import EthnicityDeleteDialog from './ethnicity-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={EthnicityUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={EthnicityUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={EthnicityDetail} />
      <ErrorBoundaryRoute path={match.url} component={Ethnicity} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={EthnicityDeleteDialog} />
  </>
);

export default Routes;
