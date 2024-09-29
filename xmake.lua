set_xmakever("2.9.5")

set_languages("c17", "cxx14")

add_rules("mode.debug", "mode.releasedbg", "mode.release")
add_rules("plugin.vsxmake.autoupdate")
add_rules("plugin.compile_commands.autoupdate")

if is_mode("release") then
    set_optimize("fastest")
end

target("myapplicationcpp2")
    if is_plat("android") then
        set_kind("shared")
        set_runtimes("c++_static")
    else
        set_kind("binary")
    end

    set_symbols("debug")
    set_strip("all")

    add_files("src/**.cpp")
    add_includedirs("src/")

    -- TODO(iFarbod): x64 too ?
    -- if is_plat("android") and is_arch("arm64-v8a") then
        -- add_ldflags("-z max-page-size=16384", {force = true})
        -- add_cxflags("-Wl -z max-page-size=16384", {force = true})
        add_ldflags("-z max-page-size=16384", {force = true})
    -- end
