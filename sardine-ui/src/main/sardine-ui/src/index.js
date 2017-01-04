import { browserHistory } from 'dva/router';
import dva from 'dva';

const app = dva({
  history: browserHistory,
});

// app.model(require('./models/demo'));
app.model(require('./models/Demo'));
app.model(require('./models/app'));

app.router(require('./router'));

app.start('#root');
