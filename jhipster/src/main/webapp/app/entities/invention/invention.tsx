import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntities } from './invention.reducer';
import { IInvention } from 'app/shared/model/invention.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const Invention = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const inventionList = useAppSelector(state => state.invention.entities);
  const loading = useAppSelector(state => state.invention.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="invention-heading" data-cy="InventionHeading">
        <Translate contentKey="so3InventionCalculatorApp.invention.home.title">Inventions</Translate>
        <div className="d-flex justify-content-end">
          <Button className="mr-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="so3InventionCalculatorApp.invention.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="so3InventionCalculatorApp.invention.home.createLabel">Create new Invention</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {inventionList && inventionList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="so3InventionCalculatorApp.invention.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="so3InventionCalculatorApp.invention.name">Name</Translate>
                </th>
                <th>
                  <Translate contentKey="so3InventionCalculatorApp.invention.cost">Cost</Translate>
                </th>
                <th>
                  <Translate contentKey="so3InventionCalculatorApp.invention.difficulty">Difficulty</Translate>
                </th>
                <th>
                  <Translate contentKey="so3InventionCalculatorApp.invention.discipline">Discipline</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {inventionList.map((invention, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${invention.id}`} color="link" size="sm">
                      {invention.id}
                    </Button>
                  </td>
                  <td>{invention.name}</td>
                  <td>{invention.cost}</td>
                  <td>{invention.difficulty}</td>
                  <td>{invention.discipline ? <Link to={`discipline/${invention.discipline.id}`}>{invention.discipline.id}</Link> : ''}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${invention.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${invention.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${invention.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="so3InventionCalculatorApp.invention.home.notFound">No Inventions found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Invention;
