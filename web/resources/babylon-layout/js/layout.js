/** 
 * PrimeFaces Babylon Layout
 */
PrimeFaces.widget.Babylon = PrimeFaces.widget.BaseWidget.extend({

    init: function (cfg) {
        this._super(cfg);
        this.wrapper = $(document.body).children('.layout-wrapper');
        this.topbar = this.wrapper.children('.layout-topbar');
        this.menuButton = this.topbar.children('.layout-menu-button');
        this.menuContainer = this.wrapper.children('.layout-menu-container');
        this.menu = this.jq;
        this.menulinks = this.menu.find('a');
        this.nanoContainer = this.menuContainer.find('.nano');
        this.expandedMenuitems = this.expandedMenuitems || [];

        this.topbarMenu = this.topbar.children('.topbar-menu');
        this.topbarItems = this.topbarMenu.children('li');
        this.topbarLinks = this.topbarItems.children('a');
        this.topbarMenuButton = $('#topbar-menu-button');

        this.topbarMenuClick = false;
        this.profileMenuClick = false;
        this.menuClick = false;
        this.menuActive = false;

        this.profileContainer = $('.layout-profile');
        this.profileButton = this.profileContainer.children('.layout-profile-button');
        this.profileMenu = this.profileContainer.children('.layout-profile-menu');

        var isHorizontalMenu = this.wrapper.hasClass('layout-horizontal');
        var isSlimMenu = this.wrapper.hasClass('layout-slim');

        if (!((isHorizontalMenu || isSlimMenu) && this.isDesktop())) {
            this.restoreMenuState();
        }
        
        if (!isHorizontalMenu) {
            this.initNanoScroll();
        }

        this._bindEvents();
    },
    
    initNanoScroll: function() {
        var scrollContent = this.nanoContainer.children('.nano-content');
        
        this.nanoContainer.nanoScroller({ flash: true });
        scrollContent.removeAttr('tabindex');
        
        if(this.cfg.statefulScroll) {
            var $this = this,
            scrollTopVal = $.cookie('babylon_scroll');
            if (scrollTopVal) {
                setTimeout(function() {
                    scrollContent.scrollTop(scrollTopVal);
                }, 10);
            }

            this.nanoContainer.on("update", function(event, values){
                clearTimeout($this.scrollTimeout);
                $this.scrollTimeout = setTimeout(function() {
                    $this.saveScrollState(scrollContent.scrollTop());
                }, 250);
            });
        }
    },

    _bindEvents: function () {
        var $this = this;

        this.menuContainer.on('click', function () {
            if (!$this.profileMenuClick) {
                $this.menuClick = true;
            } 
        });

        this.menuButton.off('click').on('click', function (e) {
            $this.menuClick = true;

            if ($this.isMobile()) {
                $this.wrapper.toggleClass('layout-mobile-active');
                $(document.body).toggleClass('blocked-scroll');
                
                setTimeout(function () {
                    $this.nanoContainer.nanoScroller();
                }, 450);
            }
            else {
                if ($this.isStaticMenu()) {
                    $this.wrapper.toggleClass('layout-static-inactive');
                    $this.saveStaticMenuState();
                }
                else {
                    $this.wrapper.toggleClass('layout-overlay-active');
                }
            }

            e.preventDefault();
        });

        $this.profileButton.off('click').on('click', function (e) {
            if (!$this.isHorizontalMenu() && !$this.isSlimMenu()) {
                $this.profileContainer.toggleClass('layout-profile-active');
                $this.profileMenu.slideToggle();

                setTimeout(function () {
                    $this.nanoContainer.nanoScroller();
                }, 500);
            }
            else {
                $this.profileMenuClick = true;

                if ($this.profileContainer.hasClass('layout-profile-active')) {
                    $this.profileMenu.addClass('fadeOutUp');

                    setTimeout(function () {
                        $this.profileContainer.removeClass('layout-profile-active');
                        $this.profileMenu.removeClass('fadeOutUp');
                    }, 150);
                }
                else {
                    $this.profileContainer.addClass('layout-profile-active');
                    $this.profileMenu.addClass('fadeInDown');
                }
            }

            e.preventDefault();
        });

        this.menu.find('> li').on('mouseenter', function(e) {    
            if (($this.isHorizontalMenu() || $this.isSlimMenu()) && $this.menuActive) {
                var item = $(this);
                
                if (!item.hasClass('active-menuitem')) {
                    $this.menu.find('.active-menuitem').removeClass('active-menuitem');
                    item.addClass('active-menuitem');
                }
            }
        });

        this.menulinks.on('click', function (e) {
            var link = $(this),
            item = link.parent('li'),
            submenu = item.children('ul');
            
            if ($this.isHorizontalMenu() || $this.isSlimMenu()) {
                submenu.css('display','');

                if (item.hasClass('active-menuitem')) {
                    if (submenu.length) {
                        item.removeClass('active-menuitem');
                        e.preventDefault();
                    }
    
                    if (item.parent().is($this.jq)) {
                        $this.menuActive = false;
                    }
                }
                else {
                    if (submenu.length > 0) {
                        e.preventDefault();
                    }
                    
                    item.siblings('.active-menuitem').removeClass('active-menuitem');
                    item.addClass('active-menuitem');
                    
                    if (item.parent().is($this.jq)) {
                        $this.menuActive = true;
                    }
                }
            }
            else {
                if (submenu.length) {
                    if (item.hasClass('active-menuitem')) {
                        $this.removeMenuitem(item.attr('id'));
                        
                        submenu.slideUp(400, function() {
                            item.removeClass('active-menuitem');
                        });
                    }
                    else {
                        $this.deactivateItems(item.siblings());
                        $this.addMenuitem(item.attr('id'));
                        $this.activate(item);
                    }
    
                    e.preventDefault();
                }
                else {
                    link.addClass('active-route');
                    $this.menu.find('.active-route').removeClass('active-route');
                    $this.setActiveRoute(item.attr('id'));
                }
                
                setTimeout(function () {
                    $this.nanoContainer.nanoScroller();
                }, 500);
            }
        });

        this.topbarMenuButton.on('click', function (e) {
            $this.topbarMenuClick = true;
            $this.topbarMenu.find('ul').removeClass('fadeInDown fadeOutUp');

            if ($this.topbarMenu.hasClass('topbar-menu-visible'))
                $this.hideTopMenu();
            else 
                $this.showTopMenu();

            e.preventDefault();
        });

        this.topbarLinks.on('click', function (e) {
            var link = $(this),
            item = link.parent(),
            submenu = link.next();

            $this.topbarMenuClick = true;

            item.siblings('.active-topmenuitem').removeClass('active-topmenuitem');

            if ($this.isDesktop()) {
                if (submenu.length) {
                    if (item.hasClass('active-topmenuitem')) {
                        $this.hideTopBarSubMenu(item);
                    }
                    else {
                        item.addClass('active-topmenuitem');
                        submenu.addClass('fadeInDown');
                    }
                }
            }
            else {
                item.children('ul').removeClass('fadeInDown fadeOutUp');
                item.toggleClass('active-topmenuitem');
            }

            if (link.attr('href') === '#') {
                e.preventDefault();
            }
        });

        $(document.body).on('click', function () {
            if (!$this.menuClick) {
                $this.wrapper.removeClass('layout-overlay-active layout-mobile-active');
                $(document.body).removeClass('blocked-scroll');

                if ($this.isHorizontalMenu() || $this.isSlimMenu()) {
                    $this.menu.find('.active-menuitem').removeClass('active-menuitem');
                    $this.menuActive = false;
                } 
            }

            if (!$this.topbarMenuClick) {
                $this.hideTopMenu();
                $this.hideTopBarSubMenu($this.topbarItems.filter('.active-topmenuitem'));
            }

            if (!$this.profileMenuClick && ($this.isHorizontalMenu() || $this.isSlimMenu())) {
                $this.profileContainer.removeClass('layout-profile-active');
            }

            $this.menuClick = false;
            $this.topbarMenuClick = false;
            $this.profileMenuClick = false;
        });
    },

    hideTopMenu: function () {
        if (this.topbarMenu.hasClass('topbar-menu-visible')) {
            var $this = this;
            this.topbarMenu.addClass('fadeOutUp');
    
            setTimeout(function () {
                $this.topbarMenu.removeClass('fadeOutUp topbar-menu-visible');
            }, 175);
        }
    },

    hideTopBarSubMenu: function(item) {
        var submenu = item.children('ul');
        submenu.addClass('fadeOutUp');

        setTimeout(function () {
            item.removeClass('active-topmenuitem'),
            submenu.removeClass('fadeOutUp');
        }, 150);
    },

    showTopMenu: function() {
        this.topbarMenu.addClass('topbar-menu-visible fadeInDown');
    },

    activate: function (item) {
        var submenu = item.children('ul');
        item.addClass('active-menuitem');

        if (submenu.length && !this.isHorizontalMenu() && !this.isSlimMenu()) {
            submenu.slideDown();  
        }
    },

    deactivate: function (item) {
        var submenu = item.children('ul');
        item.removeClass('active-menuitem');

        if (submenu.length && !this.isHorizontalMenu() && !this.isSlimMenu()) {
            submenu.hide();  
        }
    },

    deactivateItems: function (items) {
        var $this = this;

        for (var i = 0; i < items.length; i++) {
            var item = items.eq(i),
                submenu = item.children('ul');

            if (submenu.length) {
                if (item.hasClass('active-menuitem')) {
                    var activeSubItems = item.find('.active-menuitem');
                    item.removeClass('active-menuitem');

                    submenu.slideUp('normal', function () {
                        $(this).parent().find('.active-menuitem').each(function () {
                            $this.deactivate($(this));
                        });
                    });

                    $this.removeMenuitem(item.attr('id'));
                    activeSubItems.each(function () {
                        $this.removeMenuitem($(this).attr('id'));
                    });
                }
                else {
                    item.find('.active-menuitem').each(function () {
                        var subItem = $(this);
                        $this.deactivate(subItem);
                        $this.removeMenuitem(subItem.attr('id'));
                    });
                }
            }
            else if (item.hasClass('active-menuitem')) {
                $this.deactivate(item);
                $this.removeMenuitem(item.attr('id'));
            }
        }
    },

    removeMenuitem: function (id) {
        this.expandedMenuitems = $.grep(this.expandedMenuitems, function (value) {
            return value !== id;
        });

        this.saveMenuState();
    },

    addMenuitem: function (id) {
        if ($.inArray(id, this.expandedMenuitems) === -1) {
            this.expandedMenuitems.push(id);
        }
        this.saveMenuState();
    },

    saveMenuState: function () {
        $.cookie('babylon_expandeditems', this.expandedMenuitems.join(','), { path: '/' });
    },

    saveScrollState: function (value) {
        $.cookie('babylon_scroll', value, { path: '/' });
    },

    clearMenuState: function () {
        $.removeCookie('babylon_expandeditems', { path: '/' });
        $.removeCookie('babylon_active_route', { path: '/' });
        $.removeCookie('babylon_static_menu_inactive', { path: '/' });
        $.removeCookie('babylon_scroll', { path:'/' });
    },

    setActiveRoute: function (id) {
        $.cookie('babylon_active_route', id, { path: '/' });
    },

    saveStaticMenuState: function () {
        if (this.wrapper.hasClass('layout-static-inactive'))
            $.cookie('babylon_static_menu_inactive', 'babylon_static_menu_inactive', { path: '/' });
        else
            $.removeCookie('babylon_static_menu_inactive', { path: '/' });
    },

    isMobile: function () {
        return $(window).width() <= 896;
    },

    isStaticMenu: function () {
        return this.wrapper.hasClass('layout-static') && this.isDesktop();
    },

    isHorizontalMenu: function() {
        return this.wrapper.hasClass('layout-horizontal') && this.isDesktop();
    },

    isSlimMenu: function() {
        return this.wrapper.hasClass('layout-slim') && this.isDesktop();
    },

    isDesktop: function () {
        return window.innerWidth > 896;
    },

    restoreMenuState: function () {
        var menucookie = $.cookie('babylon_expandeditems');
        if (menucookie) {
            this.expandedMenuitems = menucookie.split(',');
            for (var i = 0; i < this.expandedMenuitems.length; i++) {
                var id = this.expandedMenuitems[i];
                if (id) {
                    var menuitem = $("#" + this.expandedMenuitems[i].replace(/:/g, "\\:"));
                    menuitem.addClass('active-menuitem');

                    var submenu = menuitem.children('ul');
                    if (submenu.length) {
                        submenu.show();
                    }
                }
            }
        }

        var activeRoute = $.cookie('babylon_active_route');
        if (activeRoute) {
            $('#' + activeRoute.replace(/:/g, "\\:")).children('a').addClass('active-route');
        }

        var staticMenuCookie = $.cookie('babylon_static_menu_inactive');
        if (staticMenuCookie) {
            this.wrapper.addClass('layout-static-inactive layout-static-inactive-restore');
        }
    }
});

/*!
 * jQuery Cookie Plugin v1.4.1
 * https://github.com/carhartl/jquery-cookie
 *
 * Copyright 2006, 2014 Klaus Hartl
 * Released under the MIT license
 */
(function (factory) {
    if (typeof define === 'function' && define.amd) {
        // AMD (Register as an anonymous module)
        define(['jquery'], factory);
    } else if (typeof exports === 'object') {
        // Node/CommonJS
        module.exports = factory(require('jquery'));
    } else {
        // Browser globals
        factory(jQuery);
    }
}(function ($) {

    var pluses = /\+/g;

    function encode(s) {
        return config.raw ? s : encodeURIComponent(s);
    }

    function decode(s) {
        return config.raw ? s : decodeURIComponent(s);
    }

    function stringifyCookieValue(value) {
        return encode(config.json ? JSON.stringify(value) : String(value));
    }

    function parseCookieValue(s) {
        if (s.indexOf('"') === 0) {
            // This is a quoted cookie as according to RFC2068, unescape...
            s = s.slice(1, -1).replace(/\\"/g, '"').replace(/\\\\/g, '\\');
        }

        try {
            // Replace server-side written pluses with spaces.
            // If we can't decode the cookie, ignore it, it's unusable.
            // If we can't parse the cookie, ignore it, it's unusable.
            s = decodeURIComponent(s.replace(pluses, ' '));
            return config.json ? JSON.parse(s) : s;
        } catch (e) { }
    }

    function read(s, converter) {
        var value = config.raw ? s : parseCookieValue(s);
        return $.isFunction(converter) ? converter(value) : value;
    }

    var config = $.cookie = function (key, value, options) {

        // Write

        if (arguments.length > 1 && !$.isFunction(value)) {
            options = $.extend({}, config.defaults, options);

            if (typeof options.expires === 'number') {
                var days = options.expires, t = options.expires = new Date();
                t.setMilliseconds(t.getMilliseconds() + days * 864e+5);
            }

            return (document.cookie = [
                encode(key), '=', stringifyCookieValue(value),
                options.expires ? '; expires=' + options.expires.toUTCString() : '', // use expires attribute, max-age is not supported by IE
                options.path ? '; path=' + options.path : '',
                options.domain ? '; domain=' + options.domain : '',
                options.secure ? '; secure' : ''
            ].join(''));
        }

        // Read

        var result = key ? undefined : {},
            // To prevent the for loop in the first place assign an empty array
            // in case there are no cookies at all. Also prevents odd result when
            // calling $.cookie().
            cookies = document.cookie ? document.cookie.split('; ') : [],
            i = 0,
            l = cookies.length;

        for (; i < l; i++) {
            var parts = cookies[i].split('='),
                name = decode(parts.shift()),
                cookie = parts.join('=');

            if (key === name) {
                // If second argument (value) is a function it's a converter...
                result = read(cookie, value);
                break;
            }

            // Prevent storing a cookie that we couldn't decode.
            if (!key && (cookie = read(cookie)) !== undefined) {
                result[name] = cookie;
            }
        }

        return result;
    };

    config.defaults = {};

    $.removeCookie = function (key, options) {
        // Must not alter options, thus extending a fresh object...
        $.cookie(key, '', $.extend({}, options, { expires: -1 }));
        return !$.cookie(key);
    };

}));

if (PrimeFaces.widget.InputSwitch) {
    PrimeFaces.widget.InputSwitch = PrimeFaces.widget.InputSwitch.extend({

        init: function (cfg) {
            this._super(cfg);

            if (this.input.prop('checked')) {
                this.jq.addClass('ui-inputswitch-checked');
            }
        },

        check: function () {
            var $this = this;

            this.input.prop('checked', true).trigger('change');
            setTimeout(function () {
                $this.jq.addClass('ui-inputswitch-checked');
            }, 100);
        },

        uncheck: function () {
            var $this = this;

            this.input.prop('checked', false).trigger('change');
            setTimeout(function () {
                $this.jq.removeClass('ui-inputswitch-checked');
            }, 100);
        }
    });
}