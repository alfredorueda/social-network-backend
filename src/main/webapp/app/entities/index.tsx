import React from 'react';
import { Switch } from 'react-router-dom';

// tslint:disable-next-line:no-unused-variable
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Profile from './profile';
import Gender from './gender';
import Ethnicity from './ethnicity';
import Relationship from './relationship';
import Location from './location';
import Invitation from './invitation';
import Block from './block';
import Chatroom from './chatroom';
import Message from './message';
/* jhipster-needle-add-route-import - JHipster will add routes here */

const Routes = ({ match }) => (
  <div>
    <Switch>
      {/* prettier-ignore */}
      <ErrorBoundaryRoute path={`${match.url}/profile`} component={Profile} />
      <ErrorBoundaryRoute path={`${match.url}/gender`} component={Gender} />
      <ErrorBoundaryRoute path={`${match.url}/ethnicity`} component={Ethnicity} />
      <ErrorBoundaryRoute path={`${match.url}/relationship`} component={Relationship} />
      <ErrorBoundaryRoute path={`${match.url}/location`} component={Location} />
      <ErrorBoundaryRoute path={`${match.url}/invitation`} component={Invitation} />
      <ErrorBoundaryRoute path={`${match.url}/block`} component={Block} />
      <ErrorBoundaryRoute path={`${match.url}/chatroom`} component={Chatroom} />
      <ErrorBoundaryRoute path={`${match.url}/message`} component={Message} />
      {/* jhipster-needle-add-route-path - JHipster will routes here */}
    </Switch>
  </div>
);

export default Routes;
