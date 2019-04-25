import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Chatroom from './chatroom';
import ChatroomDetail from './chatroom-detail';
import ChatroomUpdate from './chatroom-update';
import ChatroomDeleteDialog from './chatroom-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ChatroomUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ChatroomUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ChatroomDetail} />
      <ErrorBoundaryRoute path={match.url} component={Chatroom} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={ChatroomDeleteDialog} />
  </>
);

export default Routes;
