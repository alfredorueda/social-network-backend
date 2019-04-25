import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Invitation from './invitation';
import InvitationDetail from './invitation-detail';
import InvitationUpdate from './invitation-update';
import InvitationDeleteDialog from './invitation-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={InvitationUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={InvitationUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={InvitationDetail} />
      <ErrorBoundaryRoute path={match.url} component={Invitation} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={InvitationDeleteDialog} />
  </>
);

export default Routes;
