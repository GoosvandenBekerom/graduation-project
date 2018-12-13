import {SnotifyService} from 'ng-snotify';

// Rest error handler
export function handleRestError(err: any, notifyService: SnotifyService) {
  if (err.error && err.error.errors) {
    notifyService.error(err.error.errors[0].defaultMessage || 'Validation error');
  } else {
    if (typeof 'err' === 'string') {
      notifyService.error(err);
    } else if (err.status >= 500) {
      notifyService.error('Internal server error, try again later.');
    } else {
      notifyService.error(err.error.message);
    }
  }
}
