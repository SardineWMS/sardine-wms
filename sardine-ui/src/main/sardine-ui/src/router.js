import React from 'react';
import {
  Router,
  Route,
  IndexRedirect,
  IndexRoute
} from 'dva/router';
import NotFound from './routes/NotFound';
import Home from './routes/Home';
import Content from './routes/Content';
import Login from './routes/Login';
import Register from './routes/Register';
import App from './routes/app';
import Demo from './routes/Demo';

export default function({
  history
}) {
  return (
    <Router history={history}>
      <Route path="/" component={App}>
      <IndexRoute component={Demo} />
      <Route path="/users" component={Demo} />
      <Route path="/home" component={Content} />
      <Route path="/login" component={Login} />
      <Route path="/register" component={Register} />
{/*
	 * <Route path="/demo" component={App}> <IndexRoute component={Demo} />
	 * <Route path="/users" component={Demo} /> <Route path="/create"
	 * component={Create} /> <Route path="/search" component={Search} /> <Route
	 * path="/view" component={View} /> </Route>
	 */}
      </Route>	
    </Router>
  );
}