import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import DirectMessage from './direct-message';
import DirectMessageDetail from './direct-message-detail';
import DirectMessageUpdate from './direct-message-update';
import DirectMessageDeleteDialog from './direct-message-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={DirectMessageUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={DirectMessageUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={DirectMessageDetail} />
      <ErrorBoundaryRoute path={match.url} component={DirectMessage} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={DirectMessageDeleteDialog} />
  </>
);

export default Routes;
