import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntities } from './discipline.reducer';
import { IDiscipline } from 'app/shared/model/discipline.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const Discipline = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const disciplineList = useAppSelector(state => state.discipline.entities);
  const loading = useAppSelector(state => state.discipline.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="discipline-heading" data-cy="DisciplineHeading">
        <Translate contentKey="so3InventionCalculatorApp.discipline.home.title">Disciplines</Translate>
        <div className="d-flex justify-content-end">
          <Button className="mr-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="so3InventionCalculatorApp.discipline.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="so3InventionCalculatorApp.discipline.home.createLabel">Create new Discipline</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {disciplineList && disciplineList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="so3InventionCalculatorApp.discipline.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="so3InventionCalculatorApp.discipline.name">Name</Translate>
                </th>
                <th>
                  <Translate contentKey="so3InventionCalculatorApp.discipline.helperDevice">Helper Device</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {disciplineList.map((discipline, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${discipline.id}`} color="link" size="sm">
                      {discipline.id}
                    </Button>
                  </td>
                  <td>{discipline.name}</td>
                  <td>{discipline.helperDevice}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${discipline.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${discipline.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${discipline.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
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
              <Translate contentKey="so3InventionCalculatorApp.discipline.home.notFound">No Disciplines found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Discipline;
