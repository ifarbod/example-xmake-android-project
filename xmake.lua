set_xmakever("2.8.6")

set_languages("c17", "cxx20")

add_rules("mode.debug", "mode.releasedbg", "mode.release")
add_rules("plugin.vsxmake.autoupdate")
add_rules("plugin.compile_commands.autoupdate")

target("myapplicationcpp2")
    if is_plat("android") then
        set_kind("shared")
        set_runtimes("c++_static")
        add_syslinks("c++_static") -- should be add_syslinks
    else
        set_kind("binary")
    end

    add_files("src/**.cpp")
    add_includedirs("src/")
