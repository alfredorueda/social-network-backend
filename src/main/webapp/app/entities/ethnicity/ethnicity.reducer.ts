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

import { IEthnicity, defaultValue } from 'app/shared/model/ethnicity.model';

export const ACTION_TYPES = {
  FETCH_ETHNICITY_LIST: 'ethnicity/FETCH_ETHNICITY_LIST',
  FETCH_ETHNICITY: 'ethnicity/FETCH_ETHNICITY',
  CREATE_ETHNICITY: 'ethnicity/CREATE_ETHNICITY',
  UPDATE_ETHNICITY: 'ethnicity/UPDATE_ETHNICITY',
  DELETE_ETHNICITY: 'ethnicity/DELETE_ETHNICITY',
  RESET: 'ethnicity/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IEthnicity>,
  entity: defaultValue,
  links: { next: 0 },
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type EthnicityState = Readonly<typeof initialState>;

// Reducer

export default (state: EthnicityState = initialState, action): EthnicityState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_ETHNICITY_LIST):
    case REQUEST(ACTION_TYPES.FETCH_ETHNICITY):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_ETHNICITY):
    case REQUEST(ACTION_TYPES.UPDATE_ETHNICITY):
    case REQUEST(ACTION_TYPES.DELETE_ETHNICITY):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_ETHNICITY_LIST):
    case FAILURE(ACTION_TYPES.FETCH_ETHNICITY):
    case FAILURE(ACTION_TYPES.CREATE_ETHNICITY):
    case FAILURE(ACTION_TYPES.UPDATE_ETHNICITY):
    case FAILURE(ACTION_TYPES.DELETE_ETHNICITY):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_ETHNICITY_LIST):
      const links = parseHeaderForLinks(action.payload.headers.link);
      return {
        ...state,
        links,
        loading: false,
        totalItems: action.payload.headers['x-total-count'],
        entities: loadMoreDataWhenScrolled(state.entities, action.payload.data, links)
      };
    case SUCCESS(ACTION_TYPES.FETCH_ETHNICITY):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_ETHNICITY):
    case SUCCESS(ACTION_TYPES.UPDATE_ETHNICITY):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_ETHNICITY):
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

const apiUrl = 'api/ethnicities';

// Actions

export const getEntities: ICrudGetAllAction<IEthnicity> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_ETHNICITY_LIST,
    payload: axios.get<IEthnicity>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IEthnicity> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_ETHNICITY,
    payload: axios.get<IEthnicity>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IEthnicity> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_ETHNICITY,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const updateEntity: ICrudPutAction<IEthnicity> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_ETHNICITY,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IEthnicity> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_ETHNICITY,
    payload: axios.delete(requestUrl)
  });
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
