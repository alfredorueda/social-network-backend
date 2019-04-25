import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Relationship from './relationship';
import RelationshipDetail from './relationship-detail';
import RelationshipUpdate from './relationship-update';
import RelationshipDeleteDialog from './relationship-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={RelationshipUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={RelationshipUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={RelationshipDetail} />
      <ErrorBoundaryRoute path={match.url} component={Relationship} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={RelationshipDeleteDialog} />
  </>
);

export default Routes;
