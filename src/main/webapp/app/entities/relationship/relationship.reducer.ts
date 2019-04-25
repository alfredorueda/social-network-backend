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

import { IRelationship, defaultValue } from 'app/shared/model/relationship.model';

export const ACTION_TYPES = {
  FETCH_RELATIONSHIP_LIST: 'relationship/FETCH_RELATIONSHIP_LIST',
  FETCH_RELATIONSHIP: 'relationship/FETCH_RELATIONSHIP',
  CREATE_RELATIONSHIP: 'relationship/CREATE_RELATIONSHIP',
  UPDATE_RELATIONSHIP: 'relationship/UPDATE_RELATIONSHIP',
  DELETE_RELATIONSHIP: 'relationship/DELETE_RELATIONSHIP',
  RESET: 'relationship/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IRelationship>,
  entity: defaultValue,
  links: { next: 0 },
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type RelationshipState = Readonly<typeof initialState>;

// Reducer

export default (state: RelationshipState = initialState, action): RelationshipState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_RELATIONSHIP_LIST):
    case REQUEST(ACTION_TYPES.FETCH_RELATIONSHIP):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_RELATIONSHIP):
    case REQUEST(ACTION_TYPES.UPDATE_RELATIONSHIP):
    case REQUEST(ACTION_TYPES.DELETE_RELATIONSHIP):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_RELATIONSHIP_LIST):
    case FAILURE(ACTION_TYPES.FETCH_RELATIONSHIP):
    case FAILURE(ACTION_TYPES.CREATE_RELATIONSHIP):
    case FAILURE(ACTION_TYPES.UPDATE_RELATIONSHIP):
    case FAILURE(ACTION_TYPES.DELETE_RELATIONSHIP):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_RELATIONSHIP_LIST):
      const links = parseHeaderForLinks(action.payload.headers.link);
      return {
        ...state,
        links,
        loading: false,
        totalItems: action.payload.headers['x-total-count'],
        entities: loadMoreDataWhenScrolled(state.entities, action.payload.data, links)
      };
    case SUCCESS(ACTION_TYPES.FETCH_RELATIONSHIP):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_RELATIONSHIP):
    case SUCCESS(ACTION_TYPES.UPDATE_RELATIONSHIP):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_RELATIONSHIP):
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

const apiUrl = 'api/relationships';

// Actions

export const getEntities: ICrudGetAllAction<IRelationship> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_RELATIONSHIP_LIST,
    payload: axios.get<IRelationship>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IRelationship> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_RELATIONSHIP,
    payload: axios.get<IRelationship>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IRelationship> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_RELATIONSHIP,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const updateEntity: ICrudPutAction<IRelationship> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_RELATIONSHIP,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IRelationship> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_RELATIONSHIP,
    payload: axios.delete(requestUrl)
  });
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
