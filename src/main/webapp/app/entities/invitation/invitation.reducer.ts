import axios from 'axios';
import {
  parseHeaderForLinks,
  loadMoreDataWhenScrolled,
  ICrudGetAction,
  ICrudGetAllAction,
  ICrudPutAction,
  ICrudDeleteAction
} from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IInvitation, defaultValue } from 'app/shared/model/invitation.model';

export const ACTION_TYPES = {
  FETCH_INVITATION_LIST: 'invitation/FETCH_INVITATION_LIST',
  FETCH_INVITATION: 'invitation/FETCH_INVITATION',
  CREATE_INVITATION: 'invitation/CREATE_INVITATION',
  UPDATE_INVITATION: 'invitation/UPDATE_INVITATION',
  DELETE_INVITATION: 'invitation/DELETE_INVITATION',
  RESET: 'invitation/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IInvitation>,
  entity: defaultValue,
  links: { next: 0 },
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type InvitationState = Readonly<typeof initialState>;

// Reducer

export default (state: InvitationState = initialState, action): InvitationState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_INVITATION_LIST):
    case REQUEST(ACTION_TYPES.FETCH_INVITATION):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_INVITATION):
    case REQUEST(ACTION_TYPES.UPDATE_INVITATION):
    case REQUEST(ACTION_TYPES.DELETE_INVITATION):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_INVITATION_LIST):
    case FAILURE(ACTION_TYPES.FETCH_INVITATION):
    case FAILURE(ACTION_TYPES.CREATE_INVITATION):
    case FAILURE(ACTION_TYPES.UPDATE_INVITATION):
    case FAILURE(ACTION_TYPES.DELETE_INVITATION):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_INVITATION_LIST):
      const links = parseHeaderForLinks(action.payload.headers.link);
      return {
        ...state,
        links,
        loading: false,
        totalItems: action.payload.headers['x-total-count'],
        entities: loadMoreDataWhenScrolled(state.entities, action.payload.data, links)
      };
    case SUCCESS(ACTION_TYPES.FETCH_INVITATION):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_INVITATION):
    case SUCCESS(ACTION_TYPES.UPDATE_INVITATION):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_INVITATION):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {}
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState
      };
    default:
      return state;
  }
};

const apiUrl = 'api/invitations';

// Actions

export const getEntities: ICrudGetAllAction<IInvitation> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_INVITATION_LIST,
    payload: axios.get<IInvitation>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IInvitation> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_INVITATION,
    payload: axios.get<IInvitation>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IInvitation> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_INVITATION,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const updateEntity: ICrudPutAction<IInvitation> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_INVITATION,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IInvitation> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_INVITATION,
    payload: axios.delete(requestUrl)
  });
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
