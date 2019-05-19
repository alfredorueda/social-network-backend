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

import { IDirectMessage, defaultValue } from 'app/shared/model/direct-message.model';

export const ACTION_TYPES = {
  FETCH_DIRECTMESSAGE_LIST: 'directMessage/FETCH_DIRECTMESSAGE_LIST',
  FETCH_DIRECTMESSAGE: 'directMessage/FETCH_DIRECTMESSAGE',
  CREATE_DIRECTMESSAGE: 'directMessage/CREATE_DIRECTMESSAGE',
  UPDATE_DIRECTMESSAGE: 'directMessage/UPDATE_DIRECTMESSAGE',
  DELETE_DIRECTMESSAGE: 'directMessage/DELETE_DIRECTMESSAGE',
  SET_BLOB: 'directMessage/SET_BLOB',
  RESET: 'directMessage/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IDirectMessage>,
  entity: defaultValue,
  links: { next: 0 },
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type DirectMessageState = Readonly<typeof initialState>;

// Reducer

export default (state: DirectMessageState = initialState, action): DirectMessageState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_DIRECTMESSAGE_LIST):
    case REQUEST(ACTION_TYPES.FETCH_DIRECTMESSAGE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_DIRECTMESSAGE):
    case REQUEST(ACTION_TYPES.UPDATE_DIRECTMESSAGE):
    case REQUEST(ACTION_TYPES.DELETE_DIRECTMESSAGE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_DIRECTMESSAGE_LIST):
    case FAILURE(ACTION_TYPES.FETCH_DIRECTMESSAGE):
    case FAILURE(ACTION_TYPES.CREATE_DIRECTMESSAGE):
    case FAILURE(ACTION_TYPES.UPDATE_DIRECTMESSAGE):
    case FAILURE(ACTION_TYPES.DELETE_DIRECTMESSAGE):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_DIRECTMESSAGE_LIST):
      const links = parseHeaderForLinks(action.payload.headers.link);
      return {
        ...state,
        links,
        loading: false,
        totalItems: action.payload.headers['x-total-count'],
        entities: loadMoreDataWhenScrolled(state.entities, action.payload.data, links)
      };
    case SUCCESS(ACTION_TYPES.FETCH_DIRECTMESSAGE):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_DIRECTMESSAGE):
    case SUCCESS(ACTION_TYPES.UPDATE_DIRECTMESSAGE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_DIRECTMESSAGE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {}
      };
    case ACTION_TYPES.SET_BLOB:
      const { name, data, contentType } = action.payload;
      return {
        ...state,
        entity: {
          ...state.entity,
          [name]: data,
          [name + 'ContentType']: contentType
        }
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState
      };
    default:
      return state;
  }
};

const apiUrl = 'api/direct-messages';

// Actions

export const getEntities: ICrudGetAllAction<IDirectMessage> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_DIRECTMESSAGE_LIST,
    payload: axios.get<IDirectMessage>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IDirectMessage> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_DIRECTMESSAGE,
    payload: axios.get<IDirectMessage>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IDirectMessage> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_DIRECTMESSAGE,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const updateEntity: ICrudPutAction<IDirectMessage> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_DIRECTMESSAGE,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IDirectMessage> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_DIRECTMESSAGE,
    payload: axios.delete(requestUrl)
  });
  return result;
};

export const setBlob = (name, data, contentType?) => ({
  type: ACTION_TYPES.SET_BLOB,
  payload: {
    name,
    data,
    contentType
  }
});

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
