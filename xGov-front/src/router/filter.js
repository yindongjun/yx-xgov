import NProgress from 'nprogress';
import router from './index.js';
import auth from '@/common/auth.js';
import config from '@/config.js';

router.beforeEach((to, from, next) => {
    NProgress.start()
    if (JSON.parse(localStorage.getItem('menuList'))) {
      const getWhiteList = permission => {
        const a = []
        const b = permission => {
          permission.forEach(c => {
            a.push(c)
            if (c.child) {
              b(c.child)
            }
          })
        }
        b(permission)
        return a.map(k => {
          if (k.child) {
            delete k.child
          }
          return k
        })
      }
      const menu = JSON.parse(localStorage.getItem('menuList')).find(c => c.os === 'xgov')
      const whiteList = getWhiteList(menu ? menu.child : [])
      if (to.path !== '/404') {
        if (whiteList.find(k => k.permission === to.path) || (whiteList.find(k => k.permission === '/metaData/default/care') && to.path.includes('/metaData/default/care')) || (whiteList.find(k => k.permission === '/property/default/maindata') && to.path.includes('/property/default/maindata')) || to.path === '/standardManage' || to.path === '/mapCollect') {
          if (to.path !== '/login') {
            if (!auth.getToken()) {
              if (config.isSplit) {
                next('/login')
              } else {
                window.location = location.origin + '/public/#/'
              }
            } else {
              next()
            }
          } else {
            next()
          }
        } else {
            if (config.isSplit) {
                if (to.path !== '/login') {
                  if (to.path === '' || to.path === '/') {
                    next(whiteList[1].permission)
                  } else {
                    next('/404')
                  }
                } else {
                    next()
                }
            } else {
              if (to.path === '' || to.path === '/') {
                next(whiteList[1].permission)
              } else {
                next('/404')
              }
            }
        }
      } else {
        next()
      }
    } else {
      if (config.isSplit) {
        if (to.path !== '/login') {
          if (!auth.getToken()) {
            next('/login')
          } else {
            next()
          }
        } else {
          next()
        }
      } else {
        if (to.path !== '/login') {
          if (!auth.getToken()) {
            window.location = location.origin + '/public/#/'
          } else {
            next()
          }
        } else {
          window.location = location.origin + '/public/#/'
        }
      }
    }
  })
router.afterEach(() => {
    NProgress.done(); //结束Progress
});
